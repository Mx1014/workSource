//
// EvhQueryEnterpriseByPhoneResponse.m
//
#import "EvhQueryEnterpriseByPhoneResponse.h"
#import "EvhEnterpriseDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryEnterpriseByPhoneResponse
//

@implementation EvhQueryEnterpriseByPhoneResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryEnterpriseByPhoneResponse* obj = [EvhQueryEnterpriseByPhoneResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _enterprises = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.enterprises) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseDTO* item in self.enterprises) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"enterprises"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"enterprises"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseDTO* item = [EvhEnterpriseDTO new];
                
                [item fromJson: itemJson];
                [self.enterprises addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
