//
// EvhUpdateAccountOrderCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhInvoiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateAccountOrderCommand
//
@interface EvhUpdateAccountOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSNumber* buyChannel;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* invoiceFlag;

@property(nonatomic, copy) NSNumber* makeOutFlag;

@property(nonatomic, strong) EvhInvoiceDTO* invoice;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

