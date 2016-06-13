//
// EvhCreateConfAccountOrderCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateConfAccountOrderCommand
//
@interface EvhCreateConfAccountOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* buyChannel;

@property(nonatomic, copy) NSNumber* quantity;

@property(nonatomic, copy) NSNumber* period;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* invoiceFlag;

@property(nonatomic, copy) NSNumber* makeOutFlag;

@property(nonatomic, strong) EvhInvoiceDTO* invoice;

@property(nonatomic, copy) NSNumber* accountCategoryId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

