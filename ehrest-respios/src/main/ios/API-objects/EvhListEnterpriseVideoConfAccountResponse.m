//
// EvhListEnterpriseVideoConfAccountResponse.m
//
#import "EvhListEnterpriseVideoConfAccountResponse.h"
#import "EvhConfAccountDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseVideoConfAccountResponse
//

@implementation EvhListEnterpriseVideoConfAccountResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListEnterpriseVideoConfAccountResponse* obj = [EvhListEnterpriseVideoConfAccountResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _confAccounts = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.nextPageAnchor)
        [jsonObject setObject: self.nextPageAnchor forKey: @"nextPageAnchor"];
    if(self.confAccounts) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhConfAccountDTO* item in self.confAccounts) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"confAccounts"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.nextPageAnchor = [jsonObject objectForKey: @"nextPageAnchor"];
        if(self.nextPageAnchor && [self.nextPageAnchor isEqual:[NSNull null]])
            self.nextPageAnchor = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"confAccounts"];
            for(id itemJson in jsonArray) {
                EvhConfAccountDTO* item = [EvhConfAccountDTO new];
                
                [item fromJson: itemJson];
                [self.confAccounts addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
