//
// EvhApproveMemberCommand.m
//
#import "EvhApproveMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApproveMemberCommand
//

@implementation EvhApproveMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhApproveMemberCommand* obj = [EvhApproveMemberCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    [super toJson: jsonObject];
    if(self.memberUid)
        [jsonObject setObject: self.memberUid forKey: @"memberUid"];
    if(self.operatorRole)
        [jsonObject setObject: self.operatorRole forKey: @"operatorRole"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.memberUid = [jsonObject objectForKey: @"memberUid"];
        if(self.memberUid && [self.memberUid isEqual:[NSNull null]])
            self.memberUid = nil;

        self.operatorRole = [jsonObject objectForKey: @"operatorRole"];
        if(self.operatorRole && [self.operatorRole isEqual:[NSNull null]])
            self.operatorRole = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
