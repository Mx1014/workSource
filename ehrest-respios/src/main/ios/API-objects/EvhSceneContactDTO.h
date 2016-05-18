//
// EvhSceneContactDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSceneContactDTO
//
@interface EvhSceneContactDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneType;

@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSNumber* entityId;

@property(nonatomic, copy) NSNumber* contactId;

@property(nonatomic, copy) NSString* contactPhone;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSString* contactAvatar;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* departmentName;

@property(nonatomic, copy) NSString* statusLine;

@property(nonatomic, copy) NSString* occupation;

@property(nonatomic, copy) NSString* initial;

@property(nonatomic, copy) NSString* fullPinyin;

@property(nonatomic, copy) NSString* fullInitial;

@property(nonatomic, copy) NSNumber* neighborhoodRelation;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

