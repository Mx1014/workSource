//
// EvhListEnterpriseWithoutVideoConfAccountResponse.m
//
#import "EvhListEnterpriseWithoutVideoConfAccountResponse.h"
#import "EvhEnterpriseWithoutVideoConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseWithoutVideoConfAccountResponse
//

@implementation EvhListEnterpriseWithoutVideoConfAccountResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseWithoutVideoConfAccountResponse* obj = [EvhListEnterpriseWithoutVideoConfAccountResponse new];
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
        for(EvhEnterpriseWithoutVideoConfAccountDTO* item in self.enterprises) {
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
                EvhEnterpriseWithoutVideoConfAccountDTO* item = [EvhEnterpriseWithoutVideoConfAccountDTO new];
                
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
