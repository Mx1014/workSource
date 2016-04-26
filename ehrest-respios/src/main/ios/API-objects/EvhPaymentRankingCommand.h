//
// EvhPaymentRankingCommand.h
// generated at 2016-04-26 18:22:53 
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

