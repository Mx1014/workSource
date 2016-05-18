//
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse.m
//
#import "EvhListFamilyBillsAndPaysByFamilyIdCommandResponse.h"
#import "EvhPmBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyBillsAndPaysByFamilyIdCommandResponse
//

@implementation EvhListFamilyBillsAndPaysByFamilyIdCommandResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListFamilyBillsAndPaysByFamilyIdCommandResponse* obj = [EvhListFamilyBillsAndPaysByFamilyIdCommandResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _requests = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.billDate)
        [jsonObject setObject: self.billDate forKey: @"billDate"];
    if(self.nextPageOffset)
        [jsonObject setObject: self.nextPageOffset forKey: @"nextPageOffset"];
    if(self.orgTelephone)
        [jsonObject setObject: self.orgTelephone forKey: @"orgTelephone"];
    if(self.orgName)
        [jsonObject setObject: self.orgName forKey: @"orgName"];
    if(self.zlTelephone)
        [jsonObject setObject: self.zlTelephone forKey: @"zlTelephone"];
    if(self.zlName)
        [jsonObject setObject: self.zlName forKey: @"zlName"];
    if(self.orgIsExist)
        [jsonObject setObject: self.orgIsExist forKey: @"orgIsExist"];
    if(self.requests) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhPmBillsDTO* item in self.requests) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"requests"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.billDate = [jsonObject objectForKey: @"billDate"];
        if(self.billDate && [self.billDate isEqual:[NSNull null]])
            self.billDate = nil;

        self.nextPageOffset = [jsonObject objectForKey: @"nextPageOffset"];
        if(self.nextPageOffset && [self.nextPageOffset isEqual:[NSNull null]])
            self.nextPageOffset = nil;

        self.orgTelephone = [jsonObject objectForKey: @"orgTelephone"];
        if(self.orgTelephone && [self.orgTelephone isEqual:[NSNull null]])
            self.orgTelephone = nil;

        self.orgName = [jsonObject objectForKey: @"orgName"];
        if(self.orgName && [self.orgName isEqual:[NSNull null]])
            self.orgName = nil;

        self.zlTelephone = [jsonObject objectForKey: @"zlTelephone"];
        if(self.zlTelephone && [self.zlTelephone isEqual:[NSNull null]])
            self.zlTelephone = nil;

        self.zlName = [jsonObject objectForKey: @"zlName"];
        if(self.zlName && [self.zlName isEqual:[NSNull null]])
            self.zlName = nil;

        self.orgIsExist = [jsonObject objectForKey: @"orgIsExist"];
        if(self.orgIsExist && [self.orgIsExist isEqual:[NSNull null]])
            self.orgIsExist = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"requests"];
            for(id itemJson in jsonArray) {
                EvhPmBillsDTO* item = [EvhPmBillsDTO new];
                
                [item fromJson: itemJson];
                [self.requests addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
