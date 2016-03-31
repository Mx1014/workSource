//
// EvhPaymentRankingCommand.h
// generated at 2016-03-31 15:43:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentRankingCommand
//
@interface EvhPaymentRankingCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* payStatus;

@property(nonatomic, copy) NSString* orderNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

