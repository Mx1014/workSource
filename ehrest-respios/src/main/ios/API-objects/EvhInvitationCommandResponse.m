//
// EvhInvitationCommandResponse.m
//
#import "EvhInvitationCommandResponse.h"
#import "EvhInvitationDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationCommandResponse
//

@implementation EvhInvitationCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhInvitationCommandResponse* obj = [EvhInvitationCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _recipientList = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.recipientList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhInvitationDTO* item in self.recipientList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"recipientList"];
    }
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"recipientList"];
            for(id itemJson in jsonArray) {
                EvhInvitationDTO* item = [EvhInvitationDTO new];
                
                [item fromJson: itemJson];
                [self.recipientList addObject: item];
            }
        }
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
