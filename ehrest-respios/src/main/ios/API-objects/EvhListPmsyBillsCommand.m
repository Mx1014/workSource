//
// EvhListPmsyBillsCommand.m
//
#import "EvhListPmsyBillsCommand.h"
#import "EvhPmsyBillType.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmsyBillsCommand
//

@implementation EvhListPmsyBillsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListPmsyBillsCommand* obj = [EvhListPmsyBillsCommand new];
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
    if(self.customerId)
        [jsonObject setObject: self.customerId forKey: @"customerId"];
    if(self.projectId)
        [jsonObject setObject: self.projectId forKey: @"projectId"];
    if(self.resourceId)
        [jsonObject setObject: self.resourceId forKey: @"resourceId"];
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.payerId)
        [jsonObject setObject: self.payerId forKey: @"payerId"];
    if(self.billType)
        [jsonObject setObject: self.billType forKey: @"billType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.customerId = [jsonObject objectForKey: @"customerId"];
        if(self.customerId && [self.customerId isEqual:[NSNull null]])
            self.customerId = nil;

        self.projectId = [jsonObject objectForKey: @"projectId"];
        if(self.projectId && [self.projectId isEqual:[NSNull null]])
            self.projectId = nil;

        self.resourceId = [jsonObject objectForKey: @"resourceId"];
        if(self.resourceId && [self.resourceId isEqual:[NSNull null]])
            self.resourceId = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.payerId = [jsonObject objectForKey: @"payerId"];
        if(self.payerId && [self.payerId isEqual:[NSNull null]])
            self.payerId = nil;

        self.billType = [jsonObject objectForKey: @"billType"];
        if(self.billType && [self.billType isEqual:[NSNull null]])
            self.billType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
