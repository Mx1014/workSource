//
// EvhListConfCategoryResponse.m
//
#import "EvhListConfCategoryResponse.h"
#import "EvhConfCategoryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfCategoryResponse
//

@implementation EvhListConfCategoryResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListConfCategoryResponse* obj = [EvhListConfCategoryResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _categories = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.enterpriseVaildAccounts)
        [jsonObject setObject: self.enterpriseVaildAccounts forKey: @"enterpriseVaildAccounts"];
    if(self.categories) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhConfCategoryDTO* item in self.categories) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"categories"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseVaildAccounts = [jsonObject objectForKey: @"enterpriseVaildAccounts"];
        if(self.enterpriseVaildAccounts && [self.enterpriseVaildAccounts isEqual:[NSNull null]])
            self.enterpriseVaildAccounts = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"categories"];
            for(id itemJson in jsonArray) {
                EvhConfCategoryDTO* item = [EvhConfCategoryDTO new];
                
                [item fromJson: itemJson];
                [self.categories addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
