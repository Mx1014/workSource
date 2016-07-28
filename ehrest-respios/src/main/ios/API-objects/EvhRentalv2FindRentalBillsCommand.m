//
// EvhRentalv2FindRentalBillsCommand.m
//
#import "EvhRentalv2FindRentalBillsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalBillsCommand
//

@implementation EvhRentalv2FindRentalBillsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRentalv2FindRentalBillsCommand* obj = [EvhRentalv2FindRentalBillsCommand new];
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
    if(self.resourceTypeId)
        [jsonObject setObject: self.resourceTypeId forKey: @"resourceTypeId"];
    if(self.pageAnchor)
        [jsonObject setObject: self.pageAnchor forKey: @"pageAnchor"];
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.billStatus)
        [jsonObject setObject: self.billStatus forKey: @"billStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.resourceTypeId = [jsonObject objectForKey: @"resourceTypeId"];
        if(self.resourceTypeId && [self.resourceTypeId isEqual:[NSNull null]])
            self.resourceTypeId = nil;

        self.pageAnchor = [jsonObject objectForKey: @"pageAnchor"];
        if(self.pageAnchor && [self.pageAnchor isEqual:[NSNull null]])
            self.pageAnchor = nil;

        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.billStatus = [jsonObject objectForKey: @"billStatus"];
        if(self.billStatus && [self.billStatus isEqual:[NSNull null]])
            self.billStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
