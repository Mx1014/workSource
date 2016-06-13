//
// EvhListEnterpriseDetailResponse.m
//
#import "EvhListEnterpriseDetailResponse.h"
#import "EvhEnterpriseDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseDetailResponse
//

@implementation EvhListEnterpriseDetailResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseDetailResponse* obj = [EvhListEnterpriseDetailResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _details = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.details) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseDetailDTO* item in self.details) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"details"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"details"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseDetailDTO* item = [EvhEnterpriseDetailDTO new];
                
                [item fromJson: itemJson];
                [self.details addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
