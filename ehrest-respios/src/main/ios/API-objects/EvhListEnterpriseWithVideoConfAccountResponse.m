//
// EvhListEnterpriseWithVideoConfAccountResponse.m
//
#import "EvhListEnterpriseWithVideoConfAccountResponse.h"
#import "EvhEnterpriseConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseWithVideoConfAccountResponse
//

@implementation EvhListEnterpriseWithVideoConfAccountResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseWithVideoConfAccountResponse* obj = [EvhListEnterpriseWithVideoConfAccountResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _enterpriseConfAccounts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.enterpriseConfAccounts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhEnterpriseConfAccountDTO* item in self.enterpriseConfAccounts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"enterpriseConfAccounts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"enterpriseConfAccounts"];
            for(id itemJson in jsonArray) {
                EvhEnterpriseConfAccountDTO* item = [EvhEnterpriseConfAccountDTO new];
                
                [item fromJson: itemJson];
                [self.enterpriseConfAccounts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
