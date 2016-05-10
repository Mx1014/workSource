//
// EvhListEnterpriseApplyEntryResponse.m
//
#import "EvhListEnterpriseApplyEntryResponse.h"
#import "EvhEnterpriseApplyEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseApplyEntryResponse
//

@implementation EvhListEnterpriseApplyEntryResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseApplyEntryResponse* obj = [EvhListEnterpriseApplyEntryResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _entrys = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.entrys) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseApplyEntryDTO* item in self.entrys) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"entrys"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"entrys"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseApplyEntryDTO* item = [EvhEnterpriseApplyEntryDTO new];
                
                [item fromJson: itemJson];
                [self.entrys addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
