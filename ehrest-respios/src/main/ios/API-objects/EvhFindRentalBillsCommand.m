//
// EvhFindRentalBillsCommand.m
//
#import "EvhFindRentalBillsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindRentalBillsCommand
//

@implementation EvhFindRentalBillsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhFindRentalBillsCommand* obj = [EvhFindRentalBillsCommand new];
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
    if(self.launchPadItemId)
        [jsonObject setObject: self.launchPadItemId forKey: @"launchPadItemId"];
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
        self.launchPadItemId = [jsonObject objectForKey: @"launchPadItemId"];
        if(self.launchPadItemId && [self.launchPadItemId isEqual:[NSNull null]])
            self.launchPadItemId = nil;

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
